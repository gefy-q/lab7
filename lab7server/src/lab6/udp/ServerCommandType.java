package lab6.udp;

import java.io.Serializable;

public enum ServerCommandType implements Serializable {
    UPDATE_DATA, FIND_INDEX, GET_INDEX, GET_INIT, SIZE, ADD, IS_EMPTY, UPDATE, REMOVE, CLEAR, INSERT, ADD_IF_MAX, REMOVE_GREATER, COUNT_BY_AGE, COUNT_LESS_THAN_WINGSPAN, ERROR;
}
