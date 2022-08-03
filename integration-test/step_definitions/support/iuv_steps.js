const assert = require('assert')
const { Given, When, Then } = require('@cucumber/cucumber')
const { healthCheckInfo, generateIUV } = require("./iuv_client");
const { randomOrg } = require("./common");

let responseToCheck;
let iuv;
let idOrg;

// Given
Given('IUV Generator Function running', async function() {
	responseToCheck = await healthCheckInfo();
	assert.strictEqual(responseToCheck.status, 200);
});

Given('an organization', async function() {
	idOrg = randomOrg();
});

// When
When('the organization ask for a valid IUV', async function() {
	responseToCheck = await generateIUV(idOrg, { "segregationCode": "5", "auxDigit": "7" });
	// save data
	iuv = responseToCheck.data.iuv;
});

When('the organization asks for a valid IUV with incorrect body request', async function() {
	responseToCheck = await generateIUV(idOrg, { "segregationCode": "5" });
	// save data
	iuv = responseToCheck.data.iuv;
});

// Then 
Then('the organization gets the status code {int}', async function(status) {
	assert.strictEqual(responseToCheck.status, status);
});

Then('the length of the iuv is {int} digits', async function(iuv_length) {
	assert.strictEqual(iuv.length, iuv_length);
});