import {ajaxLoadSounds} from "../modules/utils.js"
import {startLoader} from "../modules/dynamic-loader.js";

const card = document.getElementById("sound-card")
const container = document.getElementById("sound-card-container")
const formSearch = document.getElementById("formSearch")
const fieldSearchTitle = document.getElementById("fieldSearchTitle")
const fieldSearchOrderby = document.getElementById("fieldSearchOrderby")

let title = ""


function loadSoundsWrapper(page, append) {
    let orderby = ''

    if (fieldSearchOrderby)
        orderby = fieldSearchOrderby.options[fieldSearchOrderby.selectedIndex].value

    if (fieldSearchTitle && fieldSearchTitle.value)
        title = fieldSearchTitle.value

    return ajaxLoadSounds(card, container, title, page, append, orderby)
}

if (formSearch && fieldSearchTitle) {
    formSearch.addEventListener("submit", ev => {
        loadSoundsWrapper(0, false)
        ev.preventDefault()
    })
}

let searchParams = new URLSearchParams(window.location.search)
if (searchParams.has("title")) {
    title = searchParams.get("title")
    if (fieldSearchTitle)
        fieldSearchTitle.value = title
}

startLoader(page => loadSoundsWrapper(page, true), container)
