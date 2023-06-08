import {ajaxFormData} from "../modules/utils.js"

const form = document.getElementById("form-upload-sound")

form.addEventListener("submit", ev => {
    ajaxFormData(form).then(() => {})
    ev.preventDefault()
})