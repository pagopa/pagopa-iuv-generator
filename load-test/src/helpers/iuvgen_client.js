import http from 'k6/http';

const ciCode = "12345678";

export function getCiCode(id) {
	return ciCode + ('000'+id).slice(-3);
}

export function organizationsIuvGen(rootUrl, id, segregationCode, auxDigit) {
	const url = `${rootUrl}/${getCiCode(id)}/iuv`
	const payload = {
        "segregationCode": segregationCode,
        "auxDigit": auxDigit
	};

	return http.post(url, JSON.stringify(payload));
}
