import Toybox.Lang;

class TransmitData{
    public var _action as ButtonIdEnum;
    public var _songId as String;
    public var _currentTime as Number;
    public var _songName as String;
    public var _songArtist as String;
    public var _songLength as Number;
    public var _imageUrl as String;
    public var _playerIsPaused as Boolean;
    public var _isSongLiked as Boolean;

    public function initialize(){
        // _action = action;
        // _songId = songId;
    }

    public function fromDict(dict as Dictionary){
        _songId = dict.get("songId");
        _songName = dict.get("songName");
        _songArtist = dict.get("songArtist");
        _songLength = dict.get("songLength");
        _currentTime = dict.get("currentTime") / 1000;
        _imageUrl = dict.get("imageURL");
        _playerIsPaused = dict.get("isPlayerPaused");
        _isSongLiked = dict.get("isSongLiked");
    }


    public function makeDictToTransmit() as Dictionary{
        var dict = {"action" => _action};
        dict.put("songId",_songId);
        System.print(dict);
        return dict;
    }
}