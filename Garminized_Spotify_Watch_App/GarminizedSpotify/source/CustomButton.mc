import Toybox.Graphics;
import Toybox.Lang;
import Toybox.WatchUi;

class CustomButton extends WatchUi.Button {
    // var playDefault = new WatchUi.Bitmap({
    //         :rezId => $.Rez.Drawables.play,
    //     });
    //     var pauseDefault = new WatchUi.Bitmap({
    //         :rezId => $.Rez.Drawables.pause,
    //     });
    
    var currentStateNumber = 0;
    var imageStates as Array<Drawable>;
    var buttonId;

    //! Constructor
    //! @param options A Dictionary of check box options
    //! @option options :locX The x-coordinate for the check box
    //! @option options :locY The y-coordinate for the check box
    //! @option options :width The clip width of the check box
    //! @option options :height The clip height of the check box
    //! @option options :stateDefault The Drawable or color to display in default state
    //! @option options :stateHighlighted The Drawable or color to display in highlighted state
    //! @option options :stateSelected The Drawable or color to display in selected state
    //! @option options :stateDisabled The Drawable or color to display in disabled state
    //! @option options :stateHighlightedSelected The Drawable or color to display in the
    //!  selected and highlighted state
    public function initialize(
        options as { :locX as Number, :locY as Number, :width as Number, :height
                    as Number, :stateDefault as Graphics.ColorType or Drawable, :stateHighlighted
                    as Graphics.ColorType or Drawable, :stateSelected as Graphics.ColorType or Drawable, :stateDisabled
                    as Graphics.ColorType or Drawable, 
                    :stateSecond as Graphics.ColorType or Drawable, 
                    :identifier as Lang.Object,
                    :buttonId as ButtonIdEnum,
                    :imageStates as Array<Drawable>}
    ) {
        Button.initialize(options);
        // Set each state value to a Drawable, color/number, or null
        // stateSecond = options.get(:stateSecondd);
        // defaultImage = options.get(:stateDefault);
        imageStates = options.get(:imageStates);
        buttonId = options.get(:buttonId);
    }

    public function changeState(){
        currentStateNumber++;
        if (currentStateNumber >= imageStates.size()){
            currentStateNumber = 0;
        }
        stateDefault = imageStates[currentStateNumber];
        stateSelected = imageStates[currentStateNumber];
        stateHighlighted = imageStates[currentStateNumber];
        // if (stateDefault == pauseDefault){
        //     stateDefault = playDefault;
        //     stateSelected = playDefault;
        //     stateHighlighted = playDefault;
        // }
        // else{
        //     stateDefault = pauseDefault;
        //     stateSelected = pauseDefault;
        //     stateHighlighted = pauseDefault;
        // }
        
    }





}