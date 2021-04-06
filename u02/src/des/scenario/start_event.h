//
// Created by florian on 06.04.21.
//

#pragma once

#include "machine_event.h"
#include "management_event.h"

namespace des {
  class start_event : public machine_event {
  public:
    start_event(unsigned int max_count_of_products, time_t max_time) :
        machine_event(time(nullptr),
                      std::make_shared<machine_state>()),
        max_count_of_products_{max_count_of_products},
        max_time_{max_time} {}

    [[nodiscard]] bool terminates() const override {
      return false;
    }

    [[nodiscard]] std::vector<event_ptr> execute() override {
      auto vec = std::vector<event_ptr>();
      vec.push_back(std::make_shared<management_event>(time(nullptr), this->state_, max_count_of_products_, max_time_));
      return vec;
    }

  protected:
    [[nodiscard]] std::string details() const override {
      return event::details("Start Event");
    }

  private:
    unsigned int max_count_of_products_;
    time_t max_time_;
  };
}

