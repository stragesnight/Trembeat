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

export async function ajaxGet(url, method = "GET") {
    const response = await fetch(
        url,
        {
            method: method,
            mode: "cors",
            credentials: "same-origin",
            redirect: "follow"
        })

    if (response.status !== 200) {
        return;
    }

    return await response.json()
}

export async function ajaxLoadSounds(
    card,
    container,
    title = '',
    page = 0,
    append = false,
    orderby = '') {

    const json = await ajaxGet(`/api/get-sounds?title=${title}&page=${page}&orderby=${orderby}`)

    if (!append)
        container.innerHTML = ""

    for (let sound of json.responseObject.content) {
        let node = card.cloneNode(true)
        node.querySelector(".--d-sound-title").innerText = sound.title
        node.querySelector(".--d-sound-author").innerText = sound.author.username
        node.querySelector(".--d-sound-genre").innerText = sound.genreName
        node.querySelector(".--d-sound-src").src = `/api/get-sound-data?id=${sound.id}`
        node.querySelector(".--d-sound-cover").src = `/api/get-cover?id=${sound.cover.id}`
        container.appendChild(node)
    }

    return json.responseObject
}