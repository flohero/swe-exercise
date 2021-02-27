#include <ml5/ml5.h>
#pragma once
class flying_object: public ml5::object {
public:

	flying_object(wxPoint pos) : position{ pos } {}

	void set_position(wxPoint pos) {
		this->position = pos;
	}

	wxPoint get_position() {
		return this->position;
	}

	flying_object operator=(const flying_object obj) = default;


private:
	wxPoint position;
};