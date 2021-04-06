//
// Created by florian on 05.04.21.
//

#pragma once

#include <utility>
#include "machine_event.h"


namespace des {
  class machine_a_event : public machine_event {
  public:

    machine_a_event(time_t t, state_ptr state) :
        machine_event(t, std::move(state)) {}


    std::vector<event_ptr> execute() override {
      auto vec = std::vector<event_ptr>();
      if (this->state_->buffer_.size() < machine_event::max_buffer_) {
        product p = product{};
        p.creation_time_ = this->execution_time_;
        this->state_->buffer_.push(p);
      }

      // If buffer is full stop producing
      if (this->state_->buffer_.size() >= machine_event::max_buffer_) {
        this->state_->machine_a_ = false;
      } else {
        vec.push_back(
            std::make_shared<machine_a_event>(machine_event::get_random_future_time(),
                                              this->state_)
        );
      }
      return vec;
    }

    [[nodiscard]] bool terminates() const override {
      return false;
    }

  protected:
    [[nodiscard]] std::string details() const override {
      return event::details("Machine A Event");
    }
  };
}

