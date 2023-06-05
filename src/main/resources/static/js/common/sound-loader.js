import {ajaxLoadSounds} from "../modules/utils.js"
import {startLoader} from "../modules/dynamic-loader.js";

const card = document.getElementById("sound-card")
const container = document.getElementById("sound-card-container")
const formSearch = document.getElementById("formSearch")
const fieldSearchTitle = document.getElementById("fieldSearchTitle")
const fieldSearchOrderby = document.getElementById("fieldSearchOrderby")


function loadSoundsWrapper(page, append) {
    let orderby = fieldSearchOrderby.options[fieldSearchOrderby.selectedIndex].value
    return ajaxLoadSounds(card, container, fieldSearchTitle.value, page, append, orderby)
}

if (formSearch && fieldSearchTitle) {
    formSearch.addEventListener("submit", ev => {
        loadSoundsWrapper(0, false)
        ev.preventDefault()
    })
}

startLoader(page => loadSoundsWrapper(page, true), container)

