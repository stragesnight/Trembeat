import {ajaxFormData, ajaxLoadComments} from "../modules/utils.js";
import {startLoader} from "../modules/dynamic-loader.js";

const formComment = document.getElementById("formComment")
const container = document.getElementById("comment-container")
const card = document.getElementById("comment-card")
const soundId = document.getElementById("fieldSoundId").value


function loadCommentsWrapper(page) {
    return ajaxLoadComments(card, container, soundId, page, true)
}

formComment.addEventListener("submit", ev => {
    // TODO: append comment into container
    ajaxFormData(formComment).then(() => {})
    ev.preventDefault()
})

startLoader(loadCommentsWrapper, container)