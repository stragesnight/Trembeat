let canLoad = true
let reachedEnd = false
let currentPage = -1

let loadFunc = undefined
let container = undefined


function load(ev) {
    const endOfPage = container.getBoundingClientRect().bottom - window.innerHeight
    if (!canLoad || reachedEnd || endOfPage > 100) {
        return;
    }

    currentPage++
    loadFunc(currentPage).then(response => {
        reachedEnd = response.last
    })

    canLoad = false
    setTimeout(() => {
        canLoad = true
    }, 1000)
}

export function startLoader(_loadFunc, _container) {
    loadFunc = _loadFunc
    container = _container
    window.addEventListener("scroll", load)
    load(null)
}
