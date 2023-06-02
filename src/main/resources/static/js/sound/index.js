async function loadSounds(page = 0) {
    const response = await fetch(
        `/api/get-sounds?page=${page}`, {
            method: "GET",
            mode: "cors",
            credentials: "same-origin",
            redirect: "follow"
        })

    if (response.status !== 200) {
        return;
    }

    const container = document.getElementById("sound-card-container")
    const card = document.getElementById("sound-card")
    const json = await response.json()

    container.innerHTML = ''

    for (let sound of json) {
        let node = card.cloneNode(true)
        node.querySelector(".--d-sound-title").innerText = sound.title
        node.querySelector(".--d-sound-author").innerText = sound.author.username
        node.querySelector(".--d-sound-genre").innerText = sound.genreName
        node.querySelector(".--d-sound-src").src = `/api/get-sound-data?id=${sound.id}`
        container.appendChild(node)
    }
}

loadSounds(0).then(() => {})