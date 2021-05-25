var fetchOptions = {
    method: "GET",
    headers: {
        'Authorization': 'Bearer ' + window.sessionStorage.getItem("myJWT")
    }
}
fetch("/restservices/countries", fetchOptions).then(function (response) {
    if (response.ok) return response.json();
}).then(myJson => {
    let countryDiv = document.querySelector("#allCountries");
    for (let country of myJson) {
        countryDiv.innerHTML = countryDiv.innerHTML + country.code+ "<br/>";
    }

}).catch(error => console.log(error))
