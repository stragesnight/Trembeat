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

        this.card.querySelector(".sound-cover-container").href = `/sound/${this.sound.id}`
        this.card.querySelector(".sound-cover-container img").src = `/api/get-cover?id=${this.sound.cover.id}`
        this.card.querySelector(".sound-title-container").href = `/sound/${this.sound.id}`
        this.card.querySelector(".sound-author").innerText = this.sound.author.username
        this.card.querySelector(".sound-title").innerText = this.sound.title
        this.card.querySelector(".sound-genre").innerText = this.sound.genreName
        this.audio.src = `/api/get-sound-data?id=${this.sound.id}`

        let formBump = this.card.querySelector(".sound-form-bump")
        if (formBump) {
            this.card.querySelector(".sound-id").value = this.sound.id
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
            if (!this.isPlaying) {
                this.audio.play();
                this.buttonPlay.textContent = "\u23F8"
                requestAnimationFrame(this.whilePlaying)
                this.isPlaying = true
            } else {
                this.audio.pause();
                this.buttonPlay.textContent = "\u23F5"
                cancelAnimationFrame(this.animationFrame)
                this.isPlaying = false
            }
        });

        this.rangeSeek.value = 0
    }

    prepareAudio = () => {
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