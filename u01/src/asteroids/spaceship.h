#include "flying_object.h"
#pragma once

class spaceship : public flying_object {
public:
	spaceship() : spaceship{wxPoint()} {}

	explicit spaceship(wxPoint pos) : flying_object{ pos } {}

	explicit spaceship(int x, int y) : flying_object{ wxPoint(x, y) } {}

	
};