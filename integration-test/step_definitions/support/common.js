const axios = require("axios");
const fs = require('fs');

let rawdata = fs.readFileSync('./config/properties.json');
let properties = JSON.parse(rawdata);
const iuv_generator_host = properties.iuv_generator_host;

function get(url) {
    return axios.get(iuv_generator_host + url)
         .then(res => {
             return res;
         })
         .catch(error => {
             return error.response;
         });
}

function post(url, body) {
    return axios.post(iuv_generator_host + url, body)
        .then(res => {
            return res;
        })
        .catch(error => {
            return error.response;
        });
}

function randomOrg() {
    return (Math.round(Math.random() * 89999999) + 10000000);
}

module.exports = {get, post, randomOrg}
