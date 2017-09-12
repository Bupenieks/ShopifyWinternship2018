'use strict';

const http = require('http'),
    express = require('express'),
    path = require('path'),
    request = require('request');

const app = express(),
    server = http.Server(app);

app.set('port', process.env.PORT || 3000);
app.use(express.static(path.join(__dirname, 'public')));
app.set('view engine', 'html');

server.listen(app.get('port'), () => {
    console.log('Express server listening on port ' + app.get('port'));
});


const apiEndpoint = "https://backend-challenge-winter-2017.herokuapp.com/customers.json"
let invalidCustomers = []
let customerDictionairy = {}

const updateCustomerDictionairy = customers => {
    for (let customer in customers) {
        //
        customer["isValidated"] = false
        for (let key in customer) {
            if (!customerDictionairy[key]) {
                customerDictionairy[key] = [customer]
            } else {
                customerDictionairy[key].push(customer)
            }
        }
    }
}

const retrieveApiData = page => {
    return new Promise((resolve, reject) => {
        request(apiEndpoint + "?page=" + page, (err, result, html) => {
            if (err) {
                reject(err)
            } else {
                let constraints = result["validations"]
                let customers = result["customers"]
                updateCustomerDictionairy(customers)
                resolve(constraints, customers)
            }
        })
    })
}

const validate = (constraints, customers) => {
    // Access each constraint object's main key, and evaluate it over each customer
    for (let constraint in constraints) {
        for (let key in constraint) {
            for (let customer in customers) {

            }
        }
    }
    // iterate through customers and check the v
    for (let customer in customers) {

    }
}

app.get('/validate/', (req, res) => {

    request(apiEndpoint, (err, result, html) => {
        if (err) {
            res.status(400).send(err)
        } else {
            let body = JSON.parse(html),
                constraints = body["validations"],
                customers = body["customers"],
                pagination = body["pagination"];

            if (!customers) {
                console.log("No customers")
                res.send({})
            }

            validate(constraints, customers)

            for (let i = 1; i < body["pagination"]["total"]; i++) {
                retrieveApiData(i)
                    .then(validate)
                    .catch(msg => res.status(400).send(msg));
            }
            res.send(body)
        }


    })
})


app.use((req, res) => {
    res.status(404).send({ url: req.url });
});


