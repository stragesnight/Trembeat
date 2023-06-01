function loadSounds(page = 0) {
    const rq = new XMLHttpRequest();

    rq.onreadystatechange = () => {
        if (rq.readyState !== XMLHttpRequest.DONE)
            return;

        if (rq.status !== 200) {
            console.error("Request failed")
            return;
        }

        const container = document.getElementById("sound-card-container")
        const card = document.getElementById("sound-card")
        const json = JSON.parse(rq.responseText)

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

    rq.open("GET", `/api/get-sounds?page=${page}`, true)
    rq.send()
}

loadSounds(0)