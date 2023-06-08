import {ajaxFormData} from "../modules/utils.js"

const form = document.querySelector("form.main-form")

form.addEventListener("submit", ev => {
    ajaxFormData(form).then(() => {})
    ev.preventDefault()
})
