import {ajaxFormData} from "../modules/utils.js"

const form = document.querySelector(".main-form")

form.addEventListener("submit", ev => {
    ajaxFormData(form).then(() => {})
    ev.preventDefault()
})
