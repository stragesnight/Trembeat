import {ajaxLoadSounds, MODE_APPEND, MODE_REPLACE} from "../modules/utils.js"
import {startLoader} from "../modules/dynamic-loader.js";

const card = document.querySelector(".sound-card")
const container = document.getElementById("sound-card-container")
const formSearch = document.getElementById("formSearch")
const fieldSearchTitle = document.getElementById("fieldSearchTitle")
const fieldSearchOrderby = document.getElementById("fieldSearchOrderby")

let title = ""


function loadSoundsWrapper(page, mode) {
    let orderby = ''

    if (fieldSearchOrderby)
        orderby = fieldSearchOrderby.options[fieldSearchOrderby.selectedIndex].value

    if (fieldSearchTitle && fieldSearchTitle.value)
        title = fieldSearchTitle.value

    return ajaxLoadSounds(card, container, title, page, mode, orderby)
}

if (formSearch && fieldSearchTitle) {
    formSearch.addEventListener("submit", ev => {
        loadSoundsWrapper(0, MODE_REPLACE)
        ev.preventDefault()
    })
}

let searchParams = new URLSearchParams(window.location.search)
if (searchParams.has("title")) {
    title = searchParams.get("title")
    if (fieldSearchTitle)
        fieldSearchTitle.value = title
}

startLoader(page => loadSoundsWrapper(page, MODE_APPEND), container)
