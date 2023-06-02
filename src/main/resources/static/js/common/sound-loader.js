import {ajaxLoadSounds} from "../modules/utils.js"

const card = document.getElementById("sound-card")
const container = document.getElementById("sound-card-container")


// TODO: page selection and search
ajaxLoadSounds(card, container, 0).then(() => {})
