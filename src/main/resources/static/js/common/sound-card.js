import {DIRECTION_DOWN, initActionMenu} from "../modules/action-menu.js";

let currentlyPlayedAudio = null

class SoundCard {
    constructor(sound, card, ajaxFormData) {
        this.sound = sound
        this.card = card
        this.buttonPlay = card.querySelector(".sound-button-play")
        this.rangeSeek = card.querySelector(".sound-time-track")
        this.audio = card.querySelector(".audio-source");
        this.duration = card.querySelector(".sound-time-duration");
        this.currentTime = card.querySelector(".sound-time-play");

        this.isPlaying = false
        this.animationFrame = null

        if (this.sound) {
            this.card.querySelector(".sound-cover-container").href = `/sound/${this.sound.id}`
            this.card.querySelector(".sound-cover-container img").src = `/api/get-cover?id=${this.sound.cover.id}`
            this.card.querySelector(".sound-author").innerText = this.sound.author.username
            this.card.querySelector(".sound-author").href = `/user/${this.sound.author.id}`
            this.card.querySelector(".sound-title").innerText = this.sound.title
            this.card.querySelector(".sound-title").href = `/sound/${this.sound.id}`
            this.card.querySelector(".sound-genre").innerText = document.getElementById(`genre-${this.sound.genreName}`).innerText
            this.audio.src = `/api/get-sound-data?id=${this.sound.id}`

            for (let node of this.card.querySelectorAll(".sound-id"))
                node.value = this.sound.id
        }

        let menuSoundAction = this.card.querySelector(".menu-sound-action")
        if (menuSoundAction) {
            let btnAction = this.card.querySelector(".btn-sound-action")
            btnAction.addEventListener("click", ev => {
                ev.stopPropagation()
                initActionMenu(menuSoundAction, btnAction, DIRECTION_DOWN)
            })
        }

        let formDeleteSound  = this.card.querySelector(".sound-form-delete")
        if (formDeleteSound) {
            formDeleteSound.addEventListener("submit", ev => {
                ajaxFormData(ev.target)
                ev.preventDefault()
            })
        }

        let formBump = this.card.querySelector(".sound-form-bump")
        if (formBump) {
            formBump.addEventListener("submit", ev => {
                ajaxFormData(ev.target)
                ev.preventDefault()
            })
        }

        if (this.audio.readyState > 0) {
            this.prepareAudio()
        } else {
            this.audio.addEventListener("loadedmetadata", this.prepareAudio);
        }

        this.audio.addEventListener("progress", this.displayBufferedAmount);

        this.rangeSeek.addEventListener("input", ev => {
            this.currentTime.textContent = this.calculateTime(this.rangeSeek.value);
            if(!this.audio.paused)
                cancelAnimationFrame(this.animationFrame);
        });

        this.rangeSeek.addEventListener("change", ev => {
            this.audio.currentTime = this.rangeSeek.value;
            if(!this.audio.paused)
                requestAnimationFrame(this.whilePlaying);
        });

        this.buttonPlay.addEventListener("click", ev => {
            if (!this.isPlaying)
                this.startAudio()
            else
                this.pauseAudio()
        })

        this.card.querySelector(".btn-sound-share").addEventListener("click", ev => {
            const audioPath = this.card.querySelector(".sound-title").href
            navigator.clipboard.writeText(audioPath).then(() => {})
        })

        this.rangeSeek.value = 0
    }

    startAudio = () => {
        if (currentlyPlayedAudio !== null)
            currentlyPlayedAudio.pauseAudio()

        currentlyPlayedAudio = this
        this.audio.play();
        this.buttonPlay.textContent = "\u23F8"
        this.isPlaying = true

        requestAnimationFrame(this.whilePlaying)
    }

    pauseAudio = () => {
        if (currentlyPlayedAudio === this)
            currentlyPlayedAudio = null

        this.audio.pause();
        this.buttonPlay.textContent = "\u23F5"
        this.isPlaying = false

        cancelAnimationFrame(this.animationFrame)
    }

    prepareAudio = () => {
        if (this.audio.duration === Infinity || isNaN(this.audio.duration)) {
            this.audio.addEventListener("durationchange", this.prepareAudio)
            return
        }

        this.displayDuration();
        this.setSliderMax();
        this.displayBufferedAmount();
    }

    calculateTime = (sec) => {
        const minutes = Math.floor(sec / 60);
        const seconds = Math.floor(sec % 60);
        const returnedSeconds = seconds < 10 ? `0${seconds}` : `${seconds}`;

        return `${minutes}:${returnedSeconds}`;
    }

    displayDuration = () => {
        this.duration.innerText = this.calculateTime(this.audio.duration);
    }

    setSliderMax = () => {
        this.rangeSeek.max = Math.floor(this.audio.duration);
    }

    displayBufferedAmount = () => {
        let bufferedAmount = 0
        if (this.audio.buffered.length > 0)
            bufferedAmount = Math.floor(this.audio.buffered.end(this.audio.buffered.length - 1));

        this.card.style.setProperty("--d-buffered-width", `${(bufferedAmount / this.rangeSeek.max) * 100}%`);
    }

    whilePlaying = () => {
        this.rangeSeek.value = Math.floor(this.audio.currentTime);
        this.currentTime.textContent = this.calculateTime(this.rangeSeek.value);
        this.animationFrame = requestAnimationFrame(this.whilePlaying);
    }
}

let cards = []

export function addSoundCard(sound, card, ajaxFormData) {
    cards.push(new SoundCard(sound, card, ajaxFormData))
}
