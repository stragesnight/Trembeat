import {ajaxLoadSounds} from "../modules/utils.js"

const card = document.getElementById("sound-card")
const container = document.getElementById("sound-card-container")
const formSearch = document.getElementById("formSearch")
const fieldSearchTitle = document.getElementById("fieldSearchTitle")
const fieldSearchOrderby = document.getElementById("fieldSearchOrderby")

let currentPage = 0
let currentTitle = ""
let reachedEnd = false
let canLoad = true

function loadSoundsWrapper(title, page, append) {
    let orderby = fieldSearchOrderby.options[fieldSearchOrderby.selectedIndex].value

    currentTitle = title
    currentPage = page

    ajaxLoadSounds(card, container, title, page, append, orderby).then(response => {
        reachedEnd = response.last
    })
}

if (formSearch && fieldSearchTitle) {
    formSearch.addEventListener("submit", ev => {
        loadSoundsWrapper(fieldSearchTitle.value, 0, false)
        ev.preventDefault()
    })
}

window.addEventListener("scroll", ev => {
    const endOfPage = container.getBoundingClientRect().bottom - window.innerHeight
    if (!canLoad || reachedEnd || endOfPage > 100) {
        return;
    }

    loadSoundsWrapper(currentTitle, currentPage + 1, true)

    canLoad = false
    setTimeout(() => {
        canLoad = true
    }, 1000)
})

loadSoundsWrapper('', 0, false)

