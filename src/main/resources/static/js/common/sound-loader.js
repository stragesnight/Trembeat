import {ajaxLoadSounds} from "../modules/utils.js"

const card = document.getElementById("sound-card")
const container = document.getElementById("sound-card-container")
const formSearch = document.getElementById("formSearch")
const fieldSearchTitle = document.getElementById("fieldSearchTitle")

function loadSoundsWrapper(title, page) {
    ajaxLoadSounds(card, container, title, page).then(() => {})
}

if (formSearch && fieldSearchTitle) {
    formSearch.addEventListener("submit", ev => {
        loadSoundsWrapper(fieldSearchTitle.value, 0)
        ev.preventDefault()
    })
}

loadSoundsWrapper('', 0)

