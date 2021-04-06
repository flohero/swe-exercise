//
// Created by florian on 27.03.21.
//

#pragma once

#include <iostream>
#include <queue>
#include <utility>
#include <unistd.h>
#include "events/event.h"

namespace des {
  class simulator {
  public:

    using event_ptr = std::shared_ptr<event>;

    /**
     *
     * @param init_list
     * @param wait_time
     */
    simulator(std::initializer_list<event_ptr> init_list) {
      for (const auto &item : init_list) {
        this->events_.push(item);
      }
    }

    /**
     * Execute all events until queue is empty
     */
    void run() {
      while (execute_top_event()) {
        sleep(simulator::wait_time_);
      }
      this->stop();
    }

    /**
     * Execute exactly one event
     * @returns true if the simulations was terminated
     */
    bool step() {
      const bool stopped = !execute_top_event();
      if (stopped) {
        this->stop();
      }
      return stopped;
    }

  private:
    std::priority_queue<event_ptr,
        std::vector<event_ptr>,
        event::greater_than_comparator> events_;

    static const unsigned int wait_time_ = 1;

    /**
     * Executes the top event in the queue
     * @returns false if there was no event or if an event terminated the simulation
     */
    bool execute_top_event() {
      if (this->events_.empty()) {
        return false;
      }
      event_ptr e = this->events_.top();
      this->events_.pop();

      std::cout << (*e) << std::endl;
      std::vector<event_ptr> new_events = e->execute();
      for (const auto &item: new_events) {
        this->events_.push(item);
      }
      return !e->terminates();
    }

    void stop() const {
      std::cout << "Terminating Simulation!" << std::endl;
    }
  };
}

