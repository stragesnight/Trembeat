import {ajaxFormData} from "../modules/utils.js"

const form = document.getElementById("form-register-user")

form.addEventListener("submit", ev => {
    ajaxFormData(form).then(() => {})
    ev.preventDefault()
})
