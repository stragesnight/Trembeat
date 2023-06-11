import {addSoundCard} from "../common/sound-card";

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

    if (response.status !== 200) {
        // TODO: error handling
        return;
    }

    try {
        const json = await response.json()
        if (json.redirectURL && json.redirectURL.length > 0)
            window.location = json.redirectURL

        return json.responseObject
    } catch (e) {
        return null
    }
}

export async function ajaxSend(url, method = "GET") {
    console.log(url)
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
    page = 0,
    append = false,
    orderby = '') {

    const json = await ajaxSend(`/api/get-sounds?title=${title}&page=${page}&orderby=${orderby}`)

    updateContainer(card, container, json.responseObject.content, append, (n, e) => {
        addSoundCard(e, n, ajaxFormData)
    })

    return json.responseObject
}

export async function ajaxLoadComments(
    card,
    container,
    soundId,
    page = 0,
    append = false) {

    const json = await ajaxSend(`/api/get-comments?soundId=${soundId}&page=${page}`)

    updateContainer(card, container, json.responseObject.content, append, (n, e) => {
        n.querySelector(".--d-comment-text").innerText = e.text
        n.querySelector(".--d-comment-username").innerText = e.user.username
        n.querySelector(".--d-comment-username").href = `/user/${e.user.id}`
        n.querySelector(".--d-comment-picture").src = `/api/get-profile-picture?id=${e.user.profilePicture.id}`
    })

    return json.responseObject
}

export function updateContainer(card, container, data, append, init) {
    if (!append)
        container.innerHTML = ""

    for (let entry of data) {
        let node = card.cloneNode(true)
        init(node, entry)
        container.appendChild(node)
    }
}
