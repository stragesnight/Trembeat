import {DIRECTION_DOWN, initActionMenu} from "../modules/action-menu.js";

const userPicture = document.getElementById("header-user-picture")
const menuHeaderProfile = document.getElementById("menu-header-profile")


userPicture.parentNode.addEventListener("click", ev => {
    ev.stopPropagation()
    initActionMenu(menuHeaderProfile, userPicture, DIRECTION_DOWN)
})
