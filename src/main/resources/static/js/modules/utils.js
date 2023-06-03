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

    if (!append)
        container.innerHTML = ""

    for (let sound of json.responseObject.content) {
        let node = card.cloneNode(true)

        node.querySelector(".--d-sound-id").value = sound.id
        node.querySelector(".--d-sound-title").innerText = sound.title
        node.querySelector(".--d-sound-author").innerText = sound.author.username
        node.querySelector(".--d-sound-genre").innerText = sound.genreName
        node.querySelector(".--d-sound-src").src = `/api/get-sound-data?id=${sound.id}`
        node.querySelector(".--d-sound-cover").src = `/api/get-cover?id=${sound.cover.id}`

        node.querySelector(".--d-sound-form-bump").addEventListener("submit", ev => {
            ajaxFormData(ev.target)
            ev.preventDefault()
        })

        container.appendChild(node)
    }

    return json.responseObject
}