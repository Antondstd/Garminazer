import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.Lang;


class GarminizedSpotifyView extends WatchUi.View {
    var _player;


    function initialize(player as Player) {
        _player = player;
        View.initialize();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        // View.findDrawableById("title").setText(Rez.Strings.AppName);
        // View.findDrawableById("message");
        // playButton = findDrawableById("play");
        // pauseButton = findDrawableById("pauseButton");
        // _player.createInterface(dc);
        if(dc has :setAntiAlias) {
            dc.setAntiAlias(true);
        }
        setLayout(_player.createInterface(dc));
        // dc.setColor(Graphics.COLOR_LT_GRAY, Graphics.COLOR_DK_GRAY);
        // dc.fillRectangle(100, 100, 100, 100);
        // setLayout(Rez.Layouts.MainLayout(dc));
    }

    // function createButton(){
    //     // Create the first check-box
    //     var playDefault = new WatchUi.Bitmap({
    //         :rezId => $.Rez.Drawables.play,
    //     });
    //     var pauseDefault = new WatchUi.Bitmap({
    //         :rezId => $.Rez.Drawables.pause,
    //     });
    //     var imagesList = new Array<Drawable>[2];
    //     imagesList[0] = playDefault;
    //     imagesList[1] = pauseDefault;
    //     var _list = new Array<CustomButton>[1];
    //     var options = {
    //         :stateDefault => playDefault,
    //         :stateHighlighted => playDefault,
    //         :stateSelected => playDefault,
    //         :stateDisabled => playDefault,
    //         :stateSecond => playDefault,
    //         :locX => 100,
    //         :locY => 100,
    //         :width => 100,
    //         :height => 100,
    //         :identifier => "playButton",
    //         :behavior => "play",
    //         :imageStates => imagesList
    //     };
    //     _list[0] = new CustomButton(options);
    //     return _list;
    // }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
        dc.setColor(Graphics.COLOR_LT_GRAY, Graphics.COLOR_BLACK);
        dc.fillRoundedRectangle(50,dc.getHeight() / 2,_player.progressBarWidth,10,5);
        dc.setColor(Graphics.COLOR_DK_GREEN,Graphics.COLOR_BLACK);
        var passed = _player.getProgressBarPassed();
        dc.fillRoundedRectangle(50,dc.getHeight() / 2,passed,10,5);
        dc.fillCircle(50 + passed, dc.getHeight() / 2 + 5, 7);
        // var train = new $.Rez.Drawables.train();
        // train.draw(dc);
        // _player.createInterface(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

    // public function makePause(){
    //     playButton.setVisible(false);
    //     pauseButton.setVisible(true);
    // }

}
