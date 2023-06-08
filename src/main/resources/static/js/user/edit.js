import {ajaxFormData} from "../modules/utils.js"

const form = document.getElementById("form-edit-user")

form.addEventListener("submit", ev => {
    ajaxFormData(form).then(() => {})
    ev.preventDefault()
})
