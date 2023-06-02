export async function ajaxFormData(form, redirect = undefined) {
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

    // TODO: get redirection url from server
    if (redirect)
        window.location = redirect

    try {
        return await response.json()
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

export async function ajaxLoadSounds(card, container, page = 0) {
    const json = await ajaxGet(`/api/get-sounds?page=${page}`)

    container.innerHTML = ""

    for (let sound of json) {
        let node = card.cloneNode(true)
        node.querySelector(".--d-sound-title").innerText = sound.title
        node.querySelector(".--d-sound-author").innerText = sound.author.username
        node.querySelector(".--d-sound-genre").innerText = sound.genreName
        node.querySelector(".--d-sound-src").src = `/api/get-sound-data?id=${sound.id}`
        container.appendChild(node)
    }
}