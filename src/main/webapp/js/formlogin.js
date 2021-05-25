function login() {
    var formData = new FormData(document.querySelector("#loginForm"));
    var encData = new URLSearchParams(formData.entries());

    fetch("/restservices/authentication", {method: "POST", body: encData})
        .then(function (response) {
            if (response.ok) return response.json(); //body will be json
            else throw "Wrong username / password"; //there is no body, just throw the error
        })
        .then(myJson => window.sessionStorage.setItem("myJWT", myJson.JWT)) //present in the JsonBody
        .catch(error => console.log(error)) //will log Wrong username/password if !response.ok
}

document.querySelector("#loginformsubmit").addEventListener("click", login);

document.querySelector("#showMeAllCountries").addEventListener("click", function (){
    var fetchOptions = { method: "GET",
    headers : {
        'Authorization' : 'Bearer ' + window.sessionStorage.getItem("myJWT")
    }}
    fetch("/restservices/countries", fetchOptions).then(function(response){
        if (response.ok) return response.json();
    }).then(myJson => console.log(myJson)).catch(error => console.log(error))
})