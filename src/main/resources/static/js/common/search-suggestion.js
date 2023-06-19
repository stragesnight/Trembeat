import {ajaxLoadSuggestions, MODE_REPLACE, updateContainer} from "../modules/utils.js";
import {closeActionMenu, DIRECTION_DROPDOWN, initActionMenu} from "../modules/action-menu.js";

const fieldSearchTitle = document.querySelector(".field-search-title")
const btnSubmitSearch = document.querySelector(".btn-submit-search")
const menuSuggestions = document.getElementById("menu-suggestions")
const itemTemplate = menuSuggestions.querySelector(".menu-item").cloneNode(true)

let isSearching = false


function onSuggestionClick(ev) {
    ev.stopPropagation()
    closeActionMenu()
    let textElement = ev.target.querySelector(".menu-item-text")
    textElement ??= ev.target
    fieldSearchTitle.value = textElement.innerText
    btnSubmitSearch.click()
}

function displaySuggestions() {
    ajaxLoadSuggestions(fieldSearchTitle.value).then(result => {
        isSearching = false

        if (result == null) {
            closeActionMenu()
            return
        }

        updateContainer(itemTemplate, menuSuggestions, result, MODE_REPLACE, (n, e) => {
            n.querySelector(".menu-item-text").innerText = e
            n.addEventListener("click", onSuggestionClick)
        })

        initActionMenu(menuSuggestions, fieldSearchTitle, DIRECTION_DROPDOWN)
    })
}

fieldSearchTitle.addEventListener("input", ev => {
    if (isSearching || !fieldSearchTitle.value) {
        closeActionMenu()
        return
    }

    isSearching = true
    setTimeout(displaySuggestions, 500)
})