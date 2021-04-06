#include <utility>
#include "end_event.h"

//
// Created by florian on 06.04.21.
//

#pragma once

#include "machine_event.h"
#include "machine_a_event.h"
#include "machine_b_event.h"

namespace des {
  class management_event : public machine_event {
  public:
    explicit management_event(time_t t, state_ptr state, unsigned int max_count_of_products, time_t max_time) :
        machine_event(t, std::move(state)),
        max_products_{max_count_of_products},
        max_time_{max_time} {}

    [[nodiscard]] std::vector<event_ptr> execute() override {
      auto vec = std::vector<event_ptr>();

      if (this->state_->store_.size() > max_products_ || time(nullptr) >= max_time_) {
        vec.push_back(std::make_shared<end_event>(this->state_));
      } else {
        if (this->state_->buffer_.size() < machine_event::max_buffer_ && !this->state_->machine_a_) {
          this->state_->machine_a_ = true;
          vec.push_back(std::make_shared<machine_a_event>(machine_event::get_random_future_time(), this->state_));
        }

        if (!this->state_->buffer_.empty() && !this->state_->machine_b_) {
          this->state_->machine_b_ = true;
          vec.push_back(std::make_shared<machine_b_event>(machine_event::get_random_future_time(), this->state_));
        }

        time_t next_management_event = this->execution_time_ + offset_;
        vec.push_back(std::make_shared<management_event>(next_management_event, this->state_, this->max_products_, this->max_time_));
      }
      return vec;
    }

    [[nodiscard]] bool terminates() const override {
      return false;
    }

  protected:
    [[nodiscard]] std::string details() const override {
      return event::details("Management Event");
    }

  private:
    static const time_t offset_ = 20;
    unsigned int max_products_;
    time_t max_time_;
  };
}

