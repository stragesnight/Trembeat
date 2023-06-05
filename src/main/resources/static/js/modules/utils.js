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
        n.querySelector(".--d-sound-id").value = e.id
        n.querySelector(".--d-sound-title").innerText = e.title
        n.querySelector(".--d-sound-author").innerText = e.author.username
        n.querySelector(".--d-sound-genre").innerText = e.genreName
        n.querySelector(".--d-sound-src").src = `/api/get-sound-data?id=${e.id}`
        n.querySelector(".--d-sound-cover").src = `/api/get-cover?id=${e.cover.id}`

        n.querySelector(".--d-sound-form-bump").addEventListener("submit", ev => {
            ajaxFormData(ev.target)
            ev.preventDefault()
        })
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
