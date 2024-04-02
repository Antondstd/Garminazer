import Toybox.Lang;
import Toybox.WatchUi;

class GarminizedSpotifyDelegate extends WatchUi.BehaviorDelegate {
    private var _player;

    function initialize(player) {
        BehaviorDelegate.initialize();
        _player = player;
        // _view = WatchUi.getCurrentView();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new GarminizedSpotifyMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
        
    }

    //! Handle the state of a Selectable changing
    //! @param event The selectable event
    //! @return true if handled, false otherwise
    public function onSelectable(event as SelectableEvent) as Boolean {
        var instance = event.getInstance();
        System.println(event.getPreviousState().toString());
        if (instance instanceof CustomButton && event.getPreviousState() == :stateDefault){
            System.println("Pressed");
            _player.onButtonPressed(instance.buttonId);
            return true;
        }
        // System.println("Test");
        return false;
    }

}