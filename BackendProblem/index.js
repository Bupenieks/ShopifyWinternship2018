"use strict";

/*
  Solutions to both the Mobile and Backend problems can also be found on my github:

  github.com/bupenieks/ShopifyWinternship2018
 */


const http = require("http"),
  express = require("express"),
  path = require("path"),
  request = require("request");

const app = express(),
  server = http.Server(app);

app.set("port", process.env.PORT || 3000);
app.use(express.static(path.join(__dirname, "public")));
app.set("view engine", "html");

server.listen(app.get("port"), () => {
  console.log("Express server listening on port " + app.get("port"));
});

const apiEndpoint = "https://backend-challenge-winter-2017.herokuapp.com/customers.json";
let invalidCustomers = [];

const retrieveApiData = page => {
  return new Promise((resolve, reject) => {
    request(apiEndpoint + "?page=" + page, (err, result, html) => {
      if (err) {
        reject(err);
      } else {
        let body = JSON.parse(html),
          constraints = body["validations"],
          customers = body["customers"];
        resolve({
          constraints: constraints,
          customers: customers
        });
      }
    });
  });
};

const validate = data => {
  // Iterate through each customer and validate each constraint key
  let customers = data.customers,
    constraints = data.constraints;

  for (let customer of customers) {
    let invalidKeys = [];
    for (let constraint of constraints) {
      for (let key in constraint) {
        if (customer[key] != null) {
          let constraintAttributes = constraint[key];
          // Loop over each constraint's specifications and test against customer
          for (let attribute in constraintAttributes) {
            switch (attribute) {
              case "type":
                if (typeof customer[key] != constraintAttributes[attribute]) {
                    invalidKeys.push(key)
                }
                break;
              case "length":
                let keyLength = customer[key].length,
                  maxLength = constraintAttributes[attribute]["max"],
                  minLength = constraintAttributes[attribute]["min"];

                if (
                  (maxLength && keyLength > maxLength) ||
                  (minLength && keyLength < minLength)
                ) {
                    invalidKeys.push(key)
                }
                break;
            }
          }
        } else if (constraint[key]["required"]) {
          invalidKeys.push(key)
        }
      }
    }
    // If we have aggregated invalid customers, add to return array
    if (invalidKeys.length != 0) {
        invalidCustomers.push({
            id: customer.id,
            invalid_fields: invalidKeys
        })
    }
  }
};

// '/validate' route initializes validation
app.get("/validate/", (req, res) => {
  // Send initial request to get pagination info, then loop
  request(apiEndpoint + "?page=1", (err, result, html) => {
    if (err) {
      res.status(400).send(err);
    } else {
      let body = JSON.parse(html),
        constraints = body["validations"],
        customers = body["customers"],
        pagination = body["pagination"];

      if (!customers) {
        console.log("No customers");
        res.send({});
        return;
      }

      validate({
        constraints: constraints,
        customers: customers
      });

      let numPages = Math.ceil(body["pagination"]["total"] / body["pagination"]["per_page"]),
        requestCount = 1,
        caught = false;

      for (let i = 2; i <= numPages; i++) {
        retrieveApiData(i)
          .then(validate)
          .then(() => {
            // send the response once all api responses have been validated and none have been rejected
            if (++requestCount == numPages && !caught) {
              res.send({invalidCustomers});
            }
          })
          .catch(msg => {
            res.status(400).send(msg);
            caught = true;
          });
      }
    }
  });
});

app.use((req, res) => {
  res.status(404).send({ url: req.url });
});
