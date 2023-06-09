let oldParent = null
let activeMenu = null

export const DIRECTION_DROPDOWN = -1
export const DIRECTION_UP = 0
export const DIRECTION_DOWN = 1
export const DIRECTION_LEFT = 2
export const DIRECTION_RIGHT = 3

const MARGIN = 32


function closeListener(ev) {
    closeActionMenu()
}

export function closeActionMenu() {
    if (activeMenu == null)
        return

    activeMenu.parentNode.removeChild(activeMenu)
    oldParent.appendChild(activeMenu)

    activeMenu = null
    oldParent = null

    document.removeEventListener("click", closeListener)
}

export function initActionMenu(menu, parent, direction) {
    if (activeMenu != null)
        closeListener(null)

    oldParent = menu.parentNode
    document.body.appendChild(menu)
    activeMenu = menu

    let parentBounds = parent.getBoundingClientRect()
    let menuBounds = menu.getBoundingClientRect()

    if (direction < DIRECTION_LEFT) {
        let left = Math.max(MARGIN, Math.min(window.innerWidth - menuBounds.width - MARGIN, parentBounds.left))
        menu.style.left = `${left}px`
    } else {
        let top = Math.max(MARGIN, Math.min(window.innerHeight - MARGIN, parentBounds.bottom))
        menu.style.top = `${top}px`
    }

    switch (direction) {
        case DIRECTION_UP:
            menu.style.top = `${parentBounds.top + menuBounds.height}px`
            break
        case DIRECTION_DROPDOWN:
            menu.style.width = `${parentBounds.width}px`
        case DIRECTION_DOWN:
            menu.style.top = `${parentBounds.bottom}px`
            break
        case DIRECTION_LEFT:
            menu.style.left = `${parentBounds.left - menuBounds.width}px`
            break
        case DIRECTION_RIGHT:
            menu.style.left = `${parentBounds.right}px`
            break
        default:
            break
    }

    document.addEventListener("click", closeListener)
}

visualViewport.addEventListener("resize", closeListener)
document.addEventListener("scroll", closeListener)
