const { get, post } = require("./common");

function healthCheckInfo() {
	return get(`/info`)
}

function generateIUV(idOrg, body) {
	return post(`/organizations/${idOrg}/iuv`, body)
}

module.exports = {
	healthCheckInfo,
	generateIUV
}
