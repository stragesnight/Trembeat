import {DIRECTION_DOWN, initActionMenu} from "../modules/action-menu.js";

const userPicture = document.getElementById("header-user-picture")
const menuHeaderProfile = document.getElementById("menu-header-profile")
const formSearch = document.getElementById("form-header-search")
const fieldTitle = document.getElementById("field-header-search-title")


if (userPicture) {
    userPicture.parentNode.addEventListener("click", ev => {
        ev.stopPropagation()
        initActionMenu(menuHeaderProfile, userPicture, DIRECTION_DOWN)
    })
}

formSearch.addEventListener("submit", ev => {
    window.location = `/sound?title=${fieldTitle.value}`
    ev.preventDefault()
})
