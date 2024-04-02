import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.Lang;
import Toybox.Communications;

class Player{
    var currentSong as SongInfo;
    var status;
    var height;
    var width;
    var progressBarWidth = 300;

    var currentImage = null;
    var currentArtist = null;
    var currentSongName = null;
    var currentTimeString;
    var timer = new Timer.Timer();
    var currentTime = 0;


    var playButton;
    var nextButton;
    var previousButton;
    var likeButton;
    var repeatButton;
    var shuffleButton;


    

    
    public function initialize(){

    }

    public function setCurrentSong(song as SongInfo){
        currentSong = song;
    }


    public function createInterface(dc){
        height = dc.getHeight();
        width = dc.getWidth();
        progressBarWidth = width - 100;
        currentSong = new SongInfo();
        currentSong.length = 135;
        initSongInfo();
        var _list = new Array[9];
        playButton = createPlayButton();
        nextButton = createNextButton();
        previousButton = createPreviousButton();
        likeButton = createLikeButton();
        repeatButton = createRepeatButton();
        // currentSong.image = createSongInfo();
        // currentImage.draw(dc);
        // currentArtist.draw(dc);
        // currentSongName.draw(dc);
        // playButton.draw(dc);
        _list[0] = currentImage;
        _list[1] = currentArtist;
        _list[2] = currentSongName;
        _list[3] = playButton;
        _list[4] = currentTimeString;
        _list[5] = nextButton;
        _list[6] = previousButton;
        _list[7] = likeButton;
        _list[8] = repeatButton;
        return _list;
    }

    function initSongInfo(){
        var artistFont20 = Graphics.getVectorFont({:face=>"RobotoCondensedRegular",:size=>20});
        var timeFont25 = Graphics.getVectorFont({:face=>"RobotoCondensedRegular",:size=>37});
        currentImage =  new WatchUi.Bitmap({
            :rezId=>Rez.Drawables.songCover,
            :locX=>WatchUi.LAYOUT_HALIGN_CENTER,
            :locY=>20,
            :width=> 10,
            :height=>20,
            :identifier => "currentSongImage"
        });
        currentArtist = new WatchUi.Text({
            :text=>"Unknown",
            :color=>Graphics.COLOR_LT_GRAY,
            :font=>artistFont20,
            :locX =>WatchUi.LAYOUT_HALIGN_CENTER,
            :locY=>145
        });
        currentSongName = new WatchUi.Text({
            :text=>"Unknown",
            :color=>Graphics.COLOR_WHITE,
            :font=>Graphics.FONT_XTINY,
            :locX =>WatchUi.LAYOUT_HALIGN_CENTER,
            :locY=>165
        });
        currentTimeString = new WatchUi.Text({
            :text=>"",
            :color=>Graphics.COLOR_WHITE,
            :font=>timeFont25,
            :locX =>WatchUi.LAYOUT_HALIGN_CENTER,
            :locY=>230
        });
        updateCurrentTime();
    }

    public function updateCurrentTime(){
        currentTimeString.setText(formatTime(currentTime) + " / " + formatTime(currentSong.length));
    }

    function formatTime(seconds) {
        var minutes = seconds / 60;
        seconds = seconds % 60; 
        var secondsFormatted = seconds > 9 ? seconds.toString() : "0" + seconds.toString();
        return minutes.toString() + ":" + secondsFormatted;
    }

    function getProgressBarPassed(){
        return currentTime * 100 / currentSong.length * progressBarWidth / 100;
    }

    function createPlayButton(){
        // Create the first check-box
        var playDefault = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.play,
        });
        var pauseDefault = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.pause,
        });
        var imagesList = new Array<Drawable>[2];
        imagesList[0] = playDefault;
        imagesList[1] = pauseDefault;
        var options = {
            :stateDefault => playDefault,
            :stateHighlighted => playDefault,
            :stateSelected => playDefault,
            :stateDisabled => playDefault,
            :stateSecond => playDefault,
            :locX => width / 2 - 50,
            :locY => 280,
            :width => 100,
            :height => 100,
            :identifier => "playButton",
            :buttonId => ButtonIdEnum.PLAY,
            :imageStates => imagesList
        };
        return new CustomButton(options);
    }

    function createLikeButton(){
        // Create the first check-box
        var likeImage = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.like,
        });
        var likedImage = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.liked,
        });
        var imagesList = new Array<Drawable>[2];
        imagesList[0] = likeImage;
        imagesList[1] = likedImage;
        var options = {
            :stateDefault => likeImage,
            :stateHighlighted => likeImage,
            :stateSelected => likeImage,
            :stateDisabled => likeImage,
            :locX => width - 80 - 50,
            :locY => 300,
            :width => 100,
            :height => 100,
            :identifier => "likeButton",
            :buttonId => ButtonIdEnum.LIKE,
            :imageStates => imagesList
        };
        return new CustomButton(options);
    }

    function createRepeatButton(){
        // Create the first check-box
        var repeatImage = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.repeat,
        });
        var repeatMoreImage = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.repeat,
        });
        var imagesList = new Array<Drawable>[2];
        imagesList[0] = repeatImage;
        imagesList[1] = repeatImage;
        var options = {
            :stateDefault => repeatImage,
            :stateHighlighted => repeatImage,
            :stateSelected => repeatImage,
            :stateDisabled => repeatImage,
            :locX => 80,
            :locY => 300,
            :width => 100,
            :height => 100,
            :identifier => "repeatButton",
            :buttonId => ButtonIdEnum.REPEAT,
            :imageStates => imagesList
        };
        return new CustomButton(options);
    }

    function createNextButton(){
        // Create the first check-box
        var defaultImage = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.next,
        });
        var imagesList = new Array<Drawable>[1];
        imagesList[0] = defaultImage;
        var options = {
            :stateDefault => defaultImage,
            :stateHighlighted => defaultImage,
            :stateSelected => defaultImage,
            :stateDisabled => defaultImage,
            :locX => 325,
            :locY => 235,
            :width => 25,
            :height => 25,
            :identifier => "nextButton",
            :buttonId => ButtonIdEnum.NEXT,
            :imageStates => imagesList
        };
        return new CustomButton(options);
    }

    function createPreviousButton(){
        // Create the first check-box
        var defaultImage = new WatchUi.Bitmap({
            :rezId => $.Rez.Drawables.previous,
        });
        var imagesList = new Array<Drawable>[1];
        imagesList[0] = defaultImage;
        var options = {
            :stateDefault => defaultImage,
            :stateHighlighted => defaultImage,
            :stateSelected => defaultImage,
            :stateDisabled => defaultImage,
            :locX => 70,
            :locY => 235,
            :width => 25,
            :height => 25,
            :identifier => "previousButton",
            :buttonId => ButtonIdEnum.PREVIOUS,
            :imageStates => imagesList
        };
        return new CustomButton(options);
    }

    public function onButtonPressed(buttonId as ButtonIdEnum){
        System.println(buttonId);
        var listener = new $.CommListener();
        if (buttonId == ButtonIdEnum.PLAY){
            var data = new TransmitData();
            data._action = ButtonIdEnum.PLAY;
            Communications.transmit(data.makeDictToTransmit(), null, listener);
            timer.start(method(:timerCallback), 1000, true);
            playButton.changeState();
            playButton.buttonId = ButtonIdEnum.PAUSE;
            // currentImage.setBitmap(Rez.Drawables.songCover2);
        }
        if (buttonId == ButtonIdEnum.PAUSE){
            var data = new TransmitData();
            data._action = ButtonIdEnum.PAUSE;
            Communications.transmit(data.makeDictToTransmit(), null, listener);
            playButton.changeState();
            timer.stop();
            playButton.buttonId = ButtonIdEnum.PLAY;
        }
        if (buttonId == ButtonIdEnum.PREVIOUS){
            var data = new TransmitData();
            data._action = ButtonIdEnum.PREVIOUS;
            Communications.transmit(data.makeDictToTransmit(), null, listener);
        }
        if (buttonId == ButtonIdEnum.NEXT){
            var data = new TransmitData();
            data._action = ButtonIdEnum.PREVIOUS;
            Communications.transmit(data.makeDictToTransmit(), null, listener);
        }
        if (buttonId == ButtonIdEnum.LIKE){
            // Communications.makeImageRequest("https://jpeg.org/images/jpegsystems-home.jpg",null,
            // {:packingFormat => Communications.PACKING_FORMAT_JPG},method(:onImageMessage));
            // Communications.makeJsonRequest("http://192.168.0.1:53777/textCheck",null,null,method(:onTextMessage));
            var data = new TransmitData();
            data._action = ButtonIdEnum.LIKE;
            data._songId = currentSong.id;
            Communications.transmit(data.makeDictToTransmit(), null, listener);
            System.println("Like pressed");
            likeButton.buttonId = ButtonIdEnum.DISLIKE;
            likeButton.changeState();
        }
        if (buttonId == ButtonIdEnum.DISLIKE){
            var data = new TransmitData();
            data._action = ButtonIdEnum.DISLIKE;
            data._songId = currentSong.id;
            Communications.transmit(data.makeDictToTransmit(), null, listener);
            likeButton.buttonId = ButtonIdEnum.LIKE;
            likeButton.changeState();
        }
    }

    function timerCallback() {
        currentTime += 1;
        updateCurrentTime();
        WatchUi.requestUpdate();
    }

    public function operateOnMessage(transmitData as TransmitData){
        System.println("Received Message1: " + transmitData);
        // if (transmitData._action == ButtonIdEnum.UPDATE){
            currentSong.id = transmitData._songId;
            System.println("Received songName: " + transmitData._songName);
            currentSongName.setText(transmitData._songName);
            System.println("Received songArtist: " + transmitData._songArtist);
            currentArtist.setText(transmitData._songArtist);
            currentSong.length = transmitData._songLength;
            System.println("Current time: " + transmitData._currentTime);
            timer.stop();
            currentTime = transmitData._currentTime;
            updateCurrentTime();
            if (!transmitData._playerIsPaused){
                timer.start(method(:timerCallback), 1000, true);
                if (playButton.buttonId == ButtonIdEnum.PLAY){
                    playButton.buttonId = ButtonIdEnum.PAUSE;
                    playButton.changeState();
                }
            }else{
                if (playButton.buttonId == ButtonIdEnum.PAUSE){
                    playButton.buttonId = ButtonIdEnum.PLAY;
                    playButton.changeState();
                }
            }
            if (transmitData._isSongLiked){
                if (likeButton.buttonId == ButtonIdEnum.LIKE){
                    likeButton.buttonId = ButtonIdEnum.DISLIKE;
                    likeButton.changeState();
                }
            }
            else{
                if (likeButton.buttonId == ButtonIdEnum.DISLIKE){
                    likeButton.buttonId = ButtonIdEnum.LIKE;
                    likeButton.changeState();
                }
            }
            WatchUi.requestUpdate();
            Communications.makeImageRequest("https://i.scdn.co/image/"+transmitData._imageUrl,null,
            {   :maxWidth => 120,
                :maxHeight => 120,
                :packingFormat => Communications.PACKING_FORMAT_JPG},method(:onImageMessage));
        // }
    }

    public function onImageMessage(responseCode, data) {
        responseCode = responseCode;
        System.println("Image responsecode: " + responseCode);
        if (responseCode == 200) {
            currentImage.setBitmap(data);
            WatchUi.requestUpdate();
        }
    }

    public function onTextMessage(responseCode, data) {
        responseCode = responseCode;
        System.println("Text responsecode: " + responseCode);
        if (responseCode == 200) {
            currentSongName.setText(data);
        }
    }

}