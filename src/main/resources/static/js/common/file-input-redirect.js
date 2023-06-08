for (let form of document.forms) {
    for (let fileInput of form.querySelectorAll("input[type=file]")) {
        let btn = form.querySelector(`#redirect-${fileInput.id}`)
        if (!btn)
            continue;

        let fileInputBuffer = fileInput
        btn.addEventListener("click", ev => {
            fileInputBuffer.click()
        })
    }
}