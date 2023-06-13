import {ajaxFormData, ajaxLoadComments, MODE_APPEND, MODE_PREPEND, updateContainer} from "../modules/utils.js";
import {startLoader} from "../modules/dynamic-loader.js";
import {addSoundCard} from "../common/sound-card.js";

const soundCard = document.querySelector(".sound-card")
const commentCard = document.querySelector(".comment-card")
const formComment = document.getElementById("form-comment")
const container = document.getElementById("comment-container")
const soundId = document.getElementById("sound-id").value


function loadCommentsWrapper(page) {
    return ajaxLoadComments(commentCard, container, soundId, page, MODE_APPEND)
}

if (formComment) {
    formComment.addEventListener("submit", ev => {
        ajaxFormData(formComment).then(comment => {
            updateContainer(commentCard, container, [ comment ], MODE_PREPEND, (n, e) => {
                n.querySelector(".comment-text").innerText = e.text
                n.querySelector(".comment-username-link").href = `/user/${e.user.id}`
                n.querySelector(".comment-username").innerText = e.user.username
                n.querySelector(".comment-picture").src = `/api/get-profile-picture?id=${e.user.profilePicture.id}`
                n.querySelector(".comment-picture-link").href = `/user/${e.user.id}`
            })

            formComment.querySelector("textarea").value = ''
        })

        ev.preventDefault()
    })
}

addSoundCard(null, soundCard, ajaxFormData)
startLoader(loadCommentsWrapper, container)