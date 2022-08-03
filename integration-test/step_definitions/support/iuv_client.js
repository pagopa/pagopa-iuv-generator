const {post} = require("./common");


function generateIUV(idOrg, body) {
    return post(`/organizations/${idOrg}/iuv`, body)
}

module.exports = {
    generateIUV
}
