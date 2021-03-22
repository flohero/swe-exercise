#pragma once
#include <ml5/ml5.h>

namespace asteroids {

	enum class rotate_direction {
		right, left
	};

	constexpr int turn_factor = 10;
	constexpr int full_degree = 360;


	class flying_object {
	public:

		using context_t = ml5::paint_event::context_t;

		explicit flying_object(wxRealPoint pos) : position_{ pos } {}

		/**
		 * Rotate the object
		 */
		virtual void rotate(rotate_direction const dir) {
			this->direction_ += dir == rotate_direction::right ? turn_factor : -turn_factor;
			if (this->direction_ < 0) {
				this->direction_ = full_degree + this->direction_;
			} else if (this->direction_ >= full_degree) {
				this->direction_ = this->direction_ - full_degree;
			}
		}

		/**
		 * Custom function for every child object
		 */
		virtual void draw(context_t& ctx) = 0;

		/*
		 * Move the object in a linear way, should be enough for most objects.
		 */
		virtual void move() {
			this->position_.x += cos(to_radiant()) * this->speed_;
			this->position_.y += sin(to_radiant()) * this->speed_;
		}

		/**
		 * Check if this object had an collision with another flying object
		 */
		[[nodiscard]] bool has_collision(const flying_object &other) const {
			auto own_shape = this->create_transformed_shape_with_offset();
			wxRegion own_region(own_shape.size(), &own_shape[0]);
			
			auto other_shape = other.create_transformed_shape_with_offset();
			wxRegion const other_region(other_shape.size(), &other_shape[0]);
			
			own_region.Intersect(other_region);
			return !own_region.IsEmpty();
		}

	protected:
		wxRealPoint position_;
		int direction_ = 0; // The angle of the object in degree
		double speed_ = 0; // Default flies with zero percent

		/**
		 * Convert the direction to a radiant from degree
		 */
		[[nodiscard]] double to_radiant() const	{
			return to_radiant(this->direction_);
		}

		static double to_radiant(int const dir) {
			return dir * ml5::util::PI / (full_degree / 2);
		}

		/*
		 * Rotate points according to the transformation matrix
		 */

		[[nodiscard]] std::vector<wxPoint> transform_points(std::vector<wxPoint> points) const {
			for (auto &point: points) {
				auto const old = point;
				point.x = old.x * cos(to_radiant()) - old.y * sin(to_radiant());
				point.y = old.x * sin(to_radiant()) + old.y * cos(to_radiant());
			}
			return points;
		}

		/**
		* if an object moves out of sight it loops back
		* at the other side of the window
		**/
		virtual void stay_in_window(context_t& ctx) {
			auto const size = ctx.GetSize();
			if (this->position_.x + this->length() < 0) {
				this->position_.x = size.x - 1.0;
			}
			if (this->position_.y + this->length() < 0) {
				this->position_.y = size.y - 1.0;
			}
			if (this->position_.x > size.x) {
				this->position_.x = this->position_.x - size.x;
			}
			if (this->position_.y > size.y) {
				this->position_.y = this->position_.y - size.y;
			}
		}

		/**
		 * Utility function to draw polygons
		 */
		virtual void do_draw(context_t &ctx) const {
			auto points_vec = this->create_shape();
			ctx.DrawPolygon(points_vec.size(), &points_vec[0], this->position_.x, this->position_.y);
		}
		
		[[nodiscard]] virtual int length() const = 0;

		/**
		 * Create a polygon with its starting point in (0/0)
		 */
		[[nodiscard]] virtual std::vector<wxPoint> create_shape() const = 0;

	private:

		/**
		 * Create a polygon for this object with its offset and transformation already calculated
		 */
		[[nodiscard]] std::vector<wxPoint> create_transformed_shape_with_offset() const {
			auto shape = this->create_shape();
			shape = this->transform_points(shape);
			for (auto& point : shape) {
				point.x += this->position_.x;
				point.y += this->position_.y;
			}
			return shape;
		}

	};
}