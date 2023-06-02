import {ajaxFormData} from "../modules/utils.js"

const form = document.getElementById("formMain")

form.addEventListener("submit", ev => {
    ajaxFormData(form).then(() => {})
    ev.preventDefault()
})
