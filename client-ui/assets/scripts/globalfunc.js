function requestOption(method, body, userToken) {
    return {
        method: method,
        mode: 'cors',
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${userToken}`
        },
        body: JSON.stringify(body)
    }
}