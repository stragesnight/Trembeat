import {ajaxFormData, ajaxLoadComments, MODE_APPEND, MODE_PREPEND, updateContainer} from "../modules/utils.js";
import {startLoader} from "../modules/dynamic-loader.js";
import {addSoundCard} from "../common/sound-card.js";

const soundCard = document.querySelector(".sound-card")
const formComment = document.getElementById("form-comment")
const container = document.getElementById("comment-container")
const commentCard = document.getElementById("comment-card")
const soundId = document.getElementById("sound-id").value


function loadCommentsWrapper(page) {
    return ajaxLoadComments(commentCard, container, soundId, page, MODE_APPEND)
}

if (formComment) {
    formComment.addEventListener("submit", ev => {
        ajaxFormData(formComment).then(comment => {
            updateContainer(commentCard, container, [ comment ], MODE_PREPEND, (n, e) => {
                n.querySelector(".--d-comment-text").innerText = e.text
                n.querySelector(".--d-comment-username").innerText = e.user.username
                n.querySelector(".--d-comment-username").href = `/user/${e.user.id}`
                n.querySelector(".--d-comment-picture").src = `/api/get-profile-picture?id=${e.user.profilePicture.id}`
            })
        })

        ev.preventDefault()
    })
}

addSoundCard(null, soundCard, ajaxFormData)
startLoader(loadCommentsWrapper, container)