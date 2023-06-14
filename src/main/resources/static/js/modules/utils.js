import {addSoundCard} from "../common/sound-card.js";

export const MODE_REPLACE = 0
export const MODE_APPEND = 1
export const MODE_PREPEND = 2

export async function ajaxFormData(form) {
    const formData = new FormData(form)

    const response = await fetch(
        form.action,
        {
            method: form.method,
            mode: "cors",
            credentials: "same-origin",
            redirect: "follow",
            body: formData
        })

    try {
        const json = await response.json()

        if (response.status !== 200) {
            for (let key in json.errors) {
                console.log(key)
                let element = document.getElementById(key + "-error")
                if (element)
                    element.innerText = json.errors[key]
            }

            return;
        }

        if (json.redirectURL && json.redirectURL.length > 0)
            window.location = json.redirectURL

        return json.responseObject
    } catch (e) {
        return null
    }
}

export async function ajaxSend(url, method = "GET") {
    const response = await fetch(
        url,
        {
            method: method,
            mode: "cors",
            credentials: "same-origin",
            redirect: "manual"
        })

    if (response.status !== 200) {
        return;
    }

    try {
        return await response.json()
    } catch (e) {
        return null
    }
}

export async function ajaxLoadSounds(
    card,
    container,
    title = '',
    authorId = null,
    page = 0,
    mode = MODE_REPLACE,
    orderby = '') {

    let requestString = `/api/get-sounds?page=${page}`
    if (title)
        requestString += `&title=${title}`
    if (authorId)
        requestString += `&authorId=${authorId}`
    if (orderby)
        requestString += `&orderby=${orderby}`

    const json = await ajaxSend(requestString)

    updateContainer(card, container, json.responseObject.content, mode, (n, e) => {
        addSoundCard(e, n, ajaxFormData)
    })

    return json.responseObject
}

export async function ajaxLoadComments(
    card,
    container,
    soundId,
    page = 0,
    mode = MODE_REPLACE) {

    const json = await ajaxSend(`/api/get-comments?soundId=${soundId}&page=${page}`)

    updateContainer(card, container, json.responseObject.content, mode, (n, e) => {
        n.querySelector(".comment-text").innerText = e.text
        n.querySelector(".comment-username-link").href = `/user/${e.user.id}`
        n.querySelector(".comment-username").innerText = e.user.username
        n.querySelector(".comment-picture").src = `/api/get-profile-picture?id=${e.user.profilePicture.id}`
        n.querySelector(".comment-picture-link").href = `/user/${e.user.id}`
    })

    return json.responseObject
}

export function updateContainer(card, container, data, mode, init) {
    if (mode === MODE_REPLACE)
        container.innerHTML = ""

    for (let entry of data) {
        let node = card.cloneNode(true)
        init(node, entry)

        if (mode === MODE_APPEND || container.childElementCount < 1) {
            container.appendChild(node)
        } else if (mode === MODE_PREPEND) {
            container.insertBefore(node, container.children.item(0))
        }
    }
}
