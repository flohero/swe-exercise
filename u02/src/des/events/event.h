//
// Created by florian on 27.03.21.
//

#pragma once

#include <ostream>
#include <chrono>
#include <memory>

namespace des {
  class event {
  public:
    friend std::ostream &operator<<(std::ostream &os, const event &e) {
      return os << e.name();
    }

    friend bool operator<(const event &e1, const event &e2) {
      return e1.execution_time_ < e2.execution_time_;
    }

    friend bool operator>(const event &e1, const event &e2) {
      return e1.execution_time_ > e2.execution_time_;
    }

    struct bigger_than_comparator {
      bool operator()(const std::shared_ptr<event>& left, const std::shared_ptr<event>& right) {
        return *left > *right;
      }
    };

    explicit event(std::time_t execution_time) :
        execution_time_{execution_time} {}

  protected:
    std::time_t execution_time_;

    [[nodiscard]] virtual std::string name() const = 0;

  private:
  };
}

