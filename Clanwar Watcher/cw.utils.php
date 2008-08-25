<?php
   /*
    * PHP port of Ruby on Rails famous distance_of_time_in_words method.
    *  See http://api.rubyonrails.com/classes/ActionView/Helpers/DateHelper.html for more details.
    *
    * Reports the approximate distance in time between two timestamps. Set include_seconds
    * to true if you want more detailed approximations.
    *
    */
    function distanceOfTimeInWords($from_time, $to_time = 0) {
        $distance_in_minutes = round(abs($to_time - $from_time) / 60);
        $distance_in_seconds = round(abs($to_time - $from_time));

        if ($distance_in_minutes <1) {
            return $distance_in_seconds.' second(s)';
        } elseif ($distance_in_minutes >= 1 and $distance_in_minutes < 44) {
            return $distance_in_minutes . ' minute(s)';
        } elseif ($distance_in_minutes >= 45 and $distance_in_minutes <= 89) {
            return 'about 1 hour';
        } elseif ($distance_in_minutes >= 90 and $distance_in_minutes <= 1439) {
            return 'about ' . round(floatval($distance_in_minutes) / 60.0) . ' hours';
        } elseif ($distance_in_minutes >= 1440 and $distance_in_minutes <= 2879) {
            return '1 day';
        } elseif ($distance_in_minutes >= 2880 and $distance_in_minutes <= 43199) {
            return 'about ' . round(floatval($distance_in_minutes) / 1440) . ' days';
        } elseif ($distance_in_minutes >= 43200 and $distance_in_minutes <= 86399) {
            return 'about 1 month';
        } elseif ($distance_in_minutes >= 86400 and $distance_in_minutes <= 525599) {
            return round(floatval($distance_in_minutes) / 43200) . ' months';
        } elseif ($distance_in_minutes >= 525600 and $distance_in_minutes <= 1051199) {
            return 'about 1 year';
        } else {
            return 'over ' . round(floatval($distance_in_minutes) / 525600) . ' years';
        }
	}
?>

<?php
	function getLevelFromCode($code){
		switch ($code) {
			case 1: return "noob";
			break;
			case 2: return "low";
			break;
			case 3: return "low+";
			break;
			case 4: return "mid";
			break;
			case 5: return "mid+";
			break;
			case 6: return "good";
			break;
			case 7: return "skilled";
			break;
			case 8: return "roxor";
			break;
			}
		return "unknown";
	}
	
	function getServerFromCode($code){
		if($code==1) return "ON";
		else if($code ==2) return "OFF";
		return "UNKNOWN";
	}
?>